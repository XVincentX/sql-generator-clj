(ns test.logical-test (:require [clojure.test :refer [deftest is]]
                                [app.logical :refer [logical-infix-operator logical-prefix-operator]]))

(deftest logical-operator-fn
  (is (= "(5 < 5) AND ('frank' = 'joe')" (logical-infix-operator [:and [:< [:field 1] 5] [:= [:field 2] "joe"]] {1 5 2 "frank"})))
  (is (= "(5 = 5) OR (5 > 'joe')" (logical-infix-operator [:or [:= [:field 1] 5] [:> [:field 1] "joe"]] {1 5 2 "frank"})))
  (is (= "(5 = 5) OR (5 > 'joe')" (logical-infix-operator [:or [:= [:field 1] 5] [:> [:field 1] "joe"]] {1 5 2 "frank"})))
  (is (= "NOT (5 = 'frank')" (logical-prefix-operator [:not [:= 5 [:field 2]]] {1 5 2 "frank"}))))
