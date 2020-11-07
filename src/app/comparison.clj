(ns app.comparison (:require [clojure.string :refer [join]]))


(defn quote-value "Returns a value suitable for a SQL statement.
                   If a string, it will quote it.
                   If a keyword, it will take its name out
                   If a number, it will leave it as it is
                   If anything else, boom."
  [value]
  (cond (string? value) (str "'" value "'")
        (keyword? value) (name value)
        (number? value) value)) ; In theory I could use a :else statement, if I'd have spec'ed the data in input.

(defn resolve-arg "Resolves a variable to its value
                   If the value is nil, NULL is returned.
                   If it's a vector [:field 1], then it will lookup the value in the fields map
                   Otherwise, it will treat the value as a constant and quote it"
  [arg fields]
  (cond
    (vector? arg) (->> arg second (get fields) quote-value)
    (nil? arg) "NULL"
    :else (quote-value arg)))

(def operators-map "Internal map of operator to their SQL text correspondence"
  {:= "IN"
   :!= "NOT IN"
   :is-empty "IS NULL"
   :not-empty "IS NOT NULL"})

(defn sql-op "Returns the appropriate SQL Operator from the allowed one in the query language"
  [operator arg]
  (cond
    (and (= operator :!=) (nil? arg)) "IS NOT"
    (and (= operator :=) (nil? arg)) "IS"
    (= operator :!=) "<>"
    :else (name operator)))

(defn comparison-operator "Generates a SQL string for a comparison operator"
  [[operator & args] fields]
  (let [args-count (count args)
        first-resolved-arg (-> args first (resolve-arg fields))]
    (cond ; maybe this could be a multi method?
      (= args-count 2) ; [:= 2 3]
      (let [second-arg (second args) second-resolved-arg (resolve-arg second-arg fields)]
        (str first-resolved-arg " " (sql-op operator second-arg) " " second-resolved-arg))

      (= args-count 1) ; [:not-empty 2]
      (str first-resolved-arg " " (operator operators-map))

      :else ; [:= 1 2 3 4]
      (str first-resolved-arg " " (operator operators-map) " (" (join "," (map #(resolve-arg % fields) (rest args))) ")"))))
