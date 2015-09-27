(defproject clojure-bot "0.1.0-SNAPSHOT"
  :description "ClojureBot - A remote REPL inside a Telegram Bot"
  :url "http://github.com/ergl/ClojureBot"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojail "1.0.6"]
                 [environ "1.0.1"]
                 [cheshire "5.5.0"]
                 [http-kit "2.1.19"]]
  :plugins [[lein-environ "1.0.0"]]
  :main clojure-bot.app)
