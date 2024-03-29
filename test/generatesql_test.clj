(ns generatesql-test (:require [clojure.test :refer [deftest is]]
                                    [app.generate-sql :refer [generate-sql]]))

(deftest generate-sql-fn
  (let [fields {1 :id 2 :name 3 :date_joined 4 :age}]
    (is (= "SELECT * FROM data WHERE date_joined IS NULL"
           (generate-sql :postgres fields {:where [:= [:field 3] nil]})))
    (is (= "SELECT * FROM data WHERE age > 35"
           (generate-sql :postgres fields {:where [:> [:field 4] 35]})))
    (is (= "SELECT * FROM data WHERE (id < 5) AND (name = 'joe')"
           (generate-sql :postgres fields {:where [:and [:< [:field 1] 5] [:= [:field 2] "joe"]]})))
    (is (= "SELECT * FROM data WHERE (date_joined <> '2015-11-01') OR (id = 456)"
           (generate-sql :postgres fields {:where [:or [:!= [:field 3] "2015-11-01"] [:= [:field 1] 456]]})))
    (is (= "SELECT * FROM data WHERE (date_joined IS NOT NULL) AND ((age > 25) OR (name = 'Jerry'))"
           (generate-sql :postgres fields {:where [:and [:!= [:field 3] nil] [:or [">" [:field 4] 25] [:= [:field 2] "Jerry"]]]})))
    (is (= "SELECT * FROM data WHERE NOT ((date_joined IS NOT NULL) AND ((age > 25) OR (name = 'Jerry')))"
           (generate-sql :postgres fields {:where [:not [:and [:!= [:field 3] nil] [:or [">" [:field 4] 25] [:= [:field 2] "Jerry"]]]]})))
    (is (= "SELECT * FROM data WHERE date_joined IN (25,26,27)"
           (generate-sql :postgres fields {:where [:= [:field 3] 25 26 27]})))
    (is (= "SELECT * FROM data WHERE name = 'cam'"
           (generate-sql :postgres fields {:where [:= [:field 2] "cam"]})))
    (is (= "SELECT * FROM data WHERE name = 'cam' LIMIT 10"
           (generate-sql :mysql fields {:where [:= [:field 2] "cam"], :limit 10})))
    (is (= "SELECT * FROM data LIMIT 20"
           (generate-sql :postgres fields {:limit 20})))
    (is (= "SELECT TOP 20 * FROM data"
           (generate-sql :sqlserver fields {:limit 20})))))
