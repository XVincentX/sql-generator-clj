(ns app.logical (:require [clojure.string :refer [join]]
                          [app.comparison :refer [comparison-operator]]))

(defmulti where-clause*
  (fn [spec _fields]
    (let [operator (first spec)]
      (cond
        (= :and operator) :logical-infix
        (= :or operator) :logical-infix
        (= :not operator) :logical-prefix
        :else :comparison))))

(defn logical-infix-operator
  [[operator & args] fields]
  (->> (map #(str "(" (where-clause* % fields) ") " (.toUpperCase (name operator))) args)
       (join " ")
       (drop-last (count (name operator)))
       (join "")
       (.trim)))

(defn logical-prefix-operator [[operator & args] fields]
  (str (.toUpperCase (name operator)) " (" (where-clause* (first args) fields) ")"))

(defmethod where-clause* :logical-prefix [spec fields] (logical-prefix-operator spec fields))
(defmethod where-clause* :logical-infix [spec fields] (logical-infix-operator spec fields))
(defmethod where-clause* :comparison [spec fields] (comparison-operator spec fields))
