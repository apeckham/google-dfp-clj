(ns google-dfp-clj.core
  (:gen-class)
  (:require [google-dfp-clj.dfp]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (google-dfp-clj.dfp/all-line-items))
