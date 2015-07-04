(ns clojure-bot.telegapi-util
  (:require [cheshire.core :refer [parse-string]]
            [clojure.walk :refer [keywordize-keys]]))


;; String -> {}
;; Return a map from the incoming result
(defn get-result-from-response [response-string]
  (-> response-string
      parse-string
      keywordize-keys
      (#(if (:ok %) (:result %) nil))))


;; Future<Any> -> {}
;; Takes the body of the incoming response and parses the result
(defn get-result-from-telegram-response [future-response]
  (-> @future-response
      (get :body)
      get-result-from-response))


;; {} -> Natural
;; Gets the update key of the incoming response
(defn get-update-key [response-block]
  (-> response-block
      (get :update_id)))


;; {} -> Natural
;; Gets the message key of the incoming response
(defn get-chat-key [response-block]
  (-> response-block
      (get :message)
      (get :chat)
      (get :id)))


;; {} -> String
;; Gets the message text of the incoming response
(defn get-response-text [response-block]
  (-> response-block
      (get :message)
      (get :text)))
