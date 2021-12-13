(ns aoc.13
  (:use clojure.pprint)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn split-on [pred coll]
  "Like split-with but does not include the delimiter in the second
  collection"
  [(take-while pred coll)
    (-> (drop-while pred coll) rest)])

(defn parse-coord [coord]
  (->> (str/split coord #",")
      (mapv #(Integer/parseInt %))))

(defn parse-fold [fold]
  (let [[axs n]
        (rest
          (re-matches #".+([xy])=(\d+)" fold))]
    [axs (Integer/parseInt n)]))

(defn parse [[nums folds]]
  [(set (map parse-coord nums))
   (vec (map parse-fold folds))])

(defn read-input [f]
  (->> (slurp f)
   str/split-lines
   (split-on (complement empty?))
   parse))