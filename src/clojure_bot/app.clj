(ns clojure-bot.app
  (:require [clojure-bot.bot :as bot])
  (:gen-class))

(defn -main [& args]
  (bot/run))
