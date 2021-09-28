(ns comparison-test (:require [clojure.test :refer [deftest is]]
                                   [app.comparison :refer [comparison-operator resolve-arg quote-value]]))

(deftest quote-value-fn
  (is (= "'hello'" (quote-value "hello")) "Returns quoted value if a string")
  (is (= "hello" (quote-value :hello)) "Returns a regular string if a keyword")
  (is (= 10 (quote-value 10))) "Returns the argument when an integer is passed")

(deftest resolve-arg-fn
  (is (= "NULL" (resolve-arg nil {})) "Returns NULL value if nil")
  (is (= "'hello'" (resolve-arg [:field 0] {0 "hello"})) "Lookups the value from the table if a vector")
  (is (= 10 (resolve-arg 10 {}))) "Returns the value if a constant")

(deftest comparison-operator-fn
  (let [data {0 "nasino" 1 "baffetto" 2 "cosciotti"}]
    (is (= "'nasino' <> 'baffetto'" (comparison-operator [:!= [:field 0] [:field 1]] data)))
    (is (= "4 <> 5" (comparison-operator [:!= 4 5] data)))
    (is (= "4 NOT IN (5,6)" (comparison-operator [:!= 4 5 6] data)))
    (is (= "'nasino' NOT IN ('baffetto','cosciotti')" (comparison-operator [:!= [:field 0] [:field 1] [:field 2]] data)))
    (is (= "4 = 5" (comparison-operator [:= 4 5] data)))
    (is (= "4 IN (5,6)" (comparison-operator [:= 4 5 6] data)))
    (is (= "4 IS NULL" (comparison-operator [:is-empty 4] data)))
    (is (= "4 IS NOT NULL" (comparison-operator [:not-empty 4] data)))))
