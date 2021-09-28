(ns app.generate-sql (:require [app.logical :refer [where-clause*]]))

(defmulti top-clause :dialect)
(defmethod top-clause :sqlserver [{:keys [limit]}]
  (if (some? limit) (str "TOP " limit) ""))
(defmethod top-clause :default [{:keys [limit]}]
  (if (some? limit) (str "LIMIT " limit) ""))

(defmulti select-clause :dialect)
(defmethod select-clause :sqlserver [{:keys [limit dialect]}]
  (str "SELECT " (top-clause {:dialect dialect :limit limit}) " * FROM data"))
(defmethod select-clause :default [_]
  (str "SELECT * FROM data "))

(defmulti where-clause "Generates a SQL statement for a where clause" (fn [spec _fields] (:dialect spec)))
(defmethod where-clause :sqlserver [{:keys [where]} fields] (when where (str "WHERE " (where-clause* where fields))))
(defmethod where-clause :default [{:keys [where]} :as spec fields]
  (.trim (str
          (if (some? where) (str "WHERE " (where-clause* where fields) " ") " ")
          (top-clause spec)))

  (defn generate-sql "Generates a full SQL Statement for the provided arguments
                    dialect: :sqlserver :mysql :postgres
                    fields: map where key is a number and then a value
                    where: map with :limit and :where clauses"
    [dialect fields {:keys [limit where]}]
    (let [t (select-clause {:dialect dialect :limit limit})
          w (where-clause {:dialect dialect :where where :limit limit} fields)]
      (str t w))))
