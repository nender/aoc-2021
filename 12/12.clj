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
            (assoc hash node (set (filter #(not= "start" %) (neighbors node pairs))))))))))

(defn lowercase? [str]
  (not (some #(Character/isUpperCase %) str)))

(defn eligible-neighbors-1 [neighbors visited]
  (set/difference
    neighbors
    (set (filter lowercase? visited))))

(def walk-1 (fn walk
  ([{nodes :nodes graph :edges}]
    (walk graph ["start"] (set (filter lowercase? nodes))))
  ([graph path to-visit]
    (let [visited (set path) cur (peek path) neighbors (graph cur)]
      (if (= "end" cur)
        [path]
        (if-let [unvisited (seq (eligible-neighbors-1 neighbors visited))]
          (mapcat #(walk graph (conj path %) to-visit) unvisited)))))))

(defn eligible-neighbors-2 [neighbors visited dup]
  (if-let [dup dup]
    (set/difference
      neighbors
      (set (filter lowercase? visited)))
    neighbors))

(def walk-2 (fn walk
  ([{nodes :nodes graph :edges}]
    (walk graph ["start"] (set (filter lowercase? nodes)) nil))
  ([graph path to-visit dup]
    (let [visited (set path) cur (peek path) neighbors (graph cur)]
      (if (= "end" cur)
        (if (some lowercase? visited)
          [path])
        (if-let [unvisited (seq (eligible-neighbors-2 neighbors visited dup))]
          (mapcat
            #(walk
               graph
               (conj path %)
               to-visit
               (if-let [dup dup]
                 dup
                 (contains? (set (filter lowercase? visited)) %)))
            unvisited)))))))

(defn part-one [input]
  (->> input
       walk-1
       count))

(defn part-two [input]
  (->> input
       walk-2
       count))

(let [graph (graph (read-input "input.txt"))]
    (println "Part one:" (part-one graph))
    (println "Part two:" (part-two graph)))