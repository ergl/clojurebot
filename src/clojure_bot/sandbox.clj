(ns clojure-bot.sandbox
  (:require [clojail.core :refer [sandbox]]
            [clojail.testers :refer [secure-tester-without-def blanket]])
  (:import java.io.StringWriter
           java.util.concurrent.TimeoutException))

;; ============================================
;; Sandbox declaration

(def clj-telegrambot-tester
  (conj secure-tester-without-def (blanket "clojure-bot")))

(def sb (sandbox clj-telegrambot-tester))


;; ============================================
;; S-expr evaluation

;; String -> {}
;; See clj-slackbot:
;; https://github.com/verma/clj-slackbot/blob/1fa7dc4e98738b06574a7453a302ef5d325a0124/src/clj_slackbot/core/handler.clj#L35
(defn eval-expr [s]
  (try
    (with-open [out (StringWriter.)]
      (let [form (binding [*read-eval* false] (read-string s))
            result (sb form {#'*out* out})]
        {:status true
         :input s
         :form form
         :result result
         :output (.toString out)}))
    (catch Exception e
      {:status false
       :input s
       :result (.getMessage e)})))
