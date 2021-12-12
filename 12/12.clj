(ns aoc.12
  (:use clojure.pprint)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn parse-line [line]
  (->> (str/split line #"-") set))

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (map parse-line)))

(defn neighbors [k pairs]
  (->> (filter #(contains? % k) pairs)
       (map #(disj % k))
       (mapcat identity)))

(defn graph-map [pairs]
  (let [all-nodes (set (mapcat identity pairs))]
    (loop [nodes all-nodes
           hash {}]
      (if (empty? nodes)
        {:nodes all-nodes, :neighbors hash}
        (let [node (first nodes)]
          (recur
            (rest nodes)
            (assoc hash node (set (neighbors node pairs)))))))))

(defn uppercase? [str]
  (not (some #(Character/isLowerCase %) str)))

(pprint (graph-map (read-input "input.txt")))