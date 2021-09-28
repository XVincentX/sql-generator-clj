(ns app.main
  (:require [app.generate-sql :refer [generate-sql]]))

(let [fields {1 :id 2 :name 3 :date_joined 4 :age}]
  (println (generate-sql :postgres {1 :id, 2 :name} {:where [:= [:field 2] "cam"]}))
  (println (generate-sql :mysql {1 :id, 2 :name} {:where [:= [:field 2] "cam"], :limit 10}))
  (println (generate-sql :postgres {1 :id, 2 :name} {:limit 20}))
  (println (generate-sql :sqlserver {1 :id, 2 :name} {:limit 20}))
  (println (generate-sql :postgres fields {:where [:= [:field 3] nil]}))
  (println (generate-sql :postgres fields {:where [:> [:field 4] 35]}))
  (println (generate-sql :postgres fields {:where [:and [:< [:field 1] 5] [:= [:field 2] "joe"]]}))
  (println (generate-sql :postgres fields {:where [:or [:!= [:field 3] "2015-11-01"] [:= [:field 1] 456]]}))
  (println (generate-sql :postgres fields {:where [:and [:!= [:field 3] nil] [:or [">" [:field 4] 25] [:= [:field 2] "Jerry"]]]}))
  (println (generate-sql :postgres fields {:where [:= [:field 4] 25 26 27]}))
  (println (generate-sql :postgres fields {:where [:= [:field 2] "cam"]}))
  (println (generate-sql :postgres fields {:where [:not [:= [:field 2] "cam"]]}))
  (println (generate-sql :mysql fields {:where [:= [:field 2] "cam"], :limit 10}))
  (println (generate-sql :postgres fields {:limit 20}))
  (println (generate-sql :sqlserver fields {:limit 20})))
