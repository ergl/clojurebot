(ns clojure-bot.bot
  (:require [clojure.string :as string]
            [clojure-bot.sandbox :as sb]
            [clojure-bot.telegapi :as tg]
            [clojure-bot.responses :as rsp]
            [clojure-bot.utils :refer [tee]]))


;; ============================================
;; Command responses

(defn- eval-command [args fmt]
  (-> args sb/eval-expr fmt))


;; String -> {:command :args}
(defn- split-command-args
  "
   Splits the incoming command string
   into commands and arguments
  "
  [command]
  (let [commands (string/split command #" " 2)]
    {:command (string/trim (first commands))
     :args (when (second commands)
             (string/trim (second commands)))}))


;; String -> String
(defn- command->response [command-string fmt]
  (let [command-map (split-command-args command-string)
        command (:command command-map)]
    (cond
      (= "/eval" command) (eval-command
                            (:args command-map) fmt)
      (or (= "/start" command)
          (= "/help" command)) rsp/start-message
      :else rsp/error-message)))


;; ============================================
;; Telegram API Integration functions

;; {} -> {}
;; Assocs to the message the appropiate response
(defn- process-update [element]
  (-> element
      (assoc :message
        (command->response
          (:message element)
          rsp/eval-response))))


;; {} -> {}
;; Map adapter for send-message
(defn- map-send [element]
  (tee element
       (#(tg/send-message (get % :chat-id) (get % :message)))))


;; Seq -> Natural
;; Processes each element of the given sequence
;; and performs the appropiate action
;;
;; Returns next update id to request to the server
(defn- seq->response [update-seq]
  (->> update-seq
       (map tg/get-response-info)
       (map process-update)
       (map map-send)
       tg/get-next-offset))


;; ============================================
;; Telegram API Integration functions

(def ^:private timeout-ms
  10000)

;; ->
(defn run []
  (let [*update-id* (atom (tg/get-last-offset))]
    (while true
      (if (seq (tg/get-update-map @*update-id*))
        (->> (seq->response (tg/get-update-map @*update-id*))
             (reset! *update-id*))
        (Thread/sleep timeout-ms)))))
