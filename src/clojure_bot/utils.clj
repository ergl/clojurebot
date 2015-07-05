(ns clojure-bot.utils)

(def log-file "log.txt")


;; String ->
(defn logger [message]
  (spit log-file (str message "\n") :append true))


;; String ->
(defn log-error [error]
  (logger (str "Uh oh! Error: " error)))


;; Natural -> String
(defn unixt->utc
  "
   Convert unix time to UTC
   formatted as yyyy-MM-dd HH:mm:ss
  "
  [t]
  (.format
    (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")
    (java.util.Date. (* 1000 t))))


;; Turns:
;; (tee val fn1 fn2 ...)
;; Into:
;; (do (->> val fn fn2 ...) val)
;; Pipes the given params into the given functions, then returns the original params
;; Useful when using dead-end functions, like writing to a file or updating a database
(defmacro tee [params & fns]
  `(do
     (->> ~params ~@fns)
     ~params))
