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
  (let [operator-name (name operator)]
    (->> args
         (map #(str "(" (where-clause* % fields) ") " (.toUpperCase operator-name)))
         (join " ")
         (drop-last (count operator-name))
         (join "")
         (.trim))))

(defn logical-prefix-operator [[operator & args] fields]
  (str (.toUpperCase (name operator)) " (" (where-clause* (first args) fields) ")"))

(defmethod where-clause* :logical-prefix [spec fields] (logical-prefix-operator spec fields))
(defmethod where-clause* :logical-infix [spec fields] (logical-infix-operator spec fields))
(defmethod where-clause* :comparison [spec fields] (comparison-operator spec fields))
