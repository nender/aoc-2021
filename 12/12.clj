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

(defn graph [pairs]
  (let [all-nodes (set (mapcat identity pairs))]
    (loop [nodes all-nodes
           hash {}]
      (if (empty? nodes)
        {:nodes all-nodes, :edges hash}
        (let [node (first nodes)]
          (recur
            (rest nodes)
            (assoc hash node (set (neighbors node pairs)))))))))

(defn lowercase? [str]
  (not (some #(Character/isUpperCase %) str)))

(defn walk [graph path to-visit]
  (let [visited (set path) cur (peek path) neighbors (graph cur)]
    (if (= "end" cur)
      [path]
      (if-let [unvisited (seq (set/difference neighbors (set (filter lowercase? visited))))]
        (mapcat #(walk graph (conj path %) to-visit) unvisited)))))

(defn valid-paths [{nodes :nodes graph :edges}]
  (walk graph ["start"] (set (filter lowercase? nodes))))

(defn part-one [input]
  (->> (graph input)
       (valid-paths)
       count))

(println "Part one:" (part-one (read-input "input.txt")))