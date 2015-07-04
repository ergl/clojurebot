(ns clojure-bot.telegapi
  (:require [clojure-bot.utils :as ut]
            [environ.core :refer [env]]
            [org.httpkit.client :as http]
            [clojure-bot.telegapi-util :as tgu]))


;; ============================================
;; Telegram API details

(def ^:private api-key (env :api-key))
(def ^:private api-url "https://api.telegram.org/bot")
(def ^:private bot-url (str api-url api-key))

(def ^:private self-url (str bot-url "/getMe"))
(def ^:private update-url (str bot-url "/getUpdates"))
(def ^:private send-message-url (str bot-url "/sendMessage"))


;; ============================================
;; Custom logger functions

;; String ->
(defn- send-success [response]
  (ut/logger (-> response
                 tgu/get-result-from-response)))


;; ============================================
;; Telegram API Integration functions

;; -> Natural
;; Gets the bot key
(defn- get-own-key []
  (-> (http/get self-url)
      tgu/get-result-from-telegram-response
      (get :id)))


;; Seq -> Natural
;; Get the next telegram API offset
(defn get-next-offset [coll]
  (-> (last coll)
      (get :update-id)
      (+ 1)))


;; Maybe<Natural> -> IO [Maybe<{}>]
(defn get-update-map
  "
   Gets all the responses queued in the server
   starting from the given update key.

   If no offset is given, request the entire queue
  "
  ([] (get-update-map 0))
  ([offset] (-> (http/get update-url
                          {:query-params {:offset offset}})
                (tgu/get-result-from-telegram-response))))


;; -> Natural
;; Gets the last update key in the server queue
(defn get-last-offset []
  (-> (last (get-update-map))
      tgu/get-update-key
      (#(if (nil? %) 0 %))))


;; Any -> {}
;; Converts the Response Element to a clojure hashmap
(defn get-response-info [response-block]
  (let [chat-id (tgu/get-chat-key response-block)
        update-id (tgu/get-update-key response-block)
        message (tgu/get-response-text response-block)]
    (hash-map
      :chat-id chat-id
      :message message
      :update-id update-id)))


;; Natural String -!>
;; Natural String fn fn -!>
(defn send-message
  "
   Send the given message to the given chat
   If supplied, execute some functions as success / error callbacks
  "
  ([chat message] (send-message chat message
                                send-success
                                ut/log-error))
  
  ([chat message success-f error-f]
   (let [options {:form-params {:chat_id chat
                                :text message}}]
     (http/post send-message-url options
                (fn [{:keys [status headers body error]}]
                  (if error
                    (error-f error)
                    (success-f body)))))))
