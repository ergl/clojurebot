(ns clojure-bot.response-format)

;; ============================================
;; S-expr evaluation formatters

;; {} -> String
;; Formats the whole expression evaluation to makrdown output
(defn markdown-format [r]
  (if (:status r)
    (str "```\n"
         ">> " (:form r) "\n"
         (when-let [o (:output r)]
           (str o "\n"))
         (str "=> " (if (nil? (:result r))
                      "nil"
                      (:result r)) "\n")
         "```")
    (str "```\n"
         "==> " (or (:form r) (:input r)) "\n"
         (str "Error:\n" (or (:result r) "Unknown Error") "\n")
         "```")))

;; {} -> String
;; Formats just the output of the evaluation
(defn simple-format [r]
  (if (:status r)
    (str (when-let [o (:output r)] o)
         "=> " (if (nil? (:result r)) "nil" (:result r)))
    (str "==> " (or (:form r) (:input r)) "\n"
         (str "Error:\n" (or (:result r) "Unknown Error") "\n"))))

;; {} -> String
;; Formats the whole expression evaluation
(defn with-input-format [r]
  (if (:status r)
    (str ">> " (:form r) "\n"
         (when-let [o (:output r)]
           (str o "\n"))
         (str "=> " (if (nil? (:result r))
                      "nil"
                      (:result r)) "\n"))
    (str "==> " (or (:form r) (:input r)) "\n"
         (str "Error:\n" (or (:result r) "Unknow Error") "\n"))))
