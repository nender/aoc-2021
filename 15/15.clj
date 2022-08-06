(ns aoc.15
  (:use clojure.pprint)
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.data.priority-map :refer [priority-map]]))

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn parse-line [line]
   (mapv #(Character/getNumericValue %) line))

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (mapv parse-line)))

(defn neighbors [graph [y x]]
  (->> [[(inc y) x] [(dec y) x]
        [y (inc x)] [y (dec x)]]
       (filter #(get-in graph %))
       set))

(defn all-coords [graph]
  (let [height (count graph)
        width (count (first graph))]
    (for [y (range height) x (range width)]
      [y x])))

(defn min-dist [unexplored dist]
  (->> dist
    (filter #(contains? unexplored (first %)))
    first))

(defn unexplored-neighbors [graph unexplored coord]
  (set/intersection
    (neighbors graph coord)
    unexplored))

(defn check-neighbors [graph neighbors dist prev coord]
  (loop [neighbors neighbors dist dist prev prev]
    (if (empty? neighbors)
      [dist prev]
      (let [cur (first neighbors)
            alt (+ (get dist coord Integer/MAX_VALUE)
                   (get-in graph cur))]
        (if (< alt (get dist cur Integer/MAX_VALUE))
          (recur (rest neighbors)
            (assoc dist cur alt)
            (assoc prev cur coord))
          (recur (rest neighbors) dist prev))))))

(defn dijkstra [graph source target]
  (loop [unexplored (set (all-coords graph))
         dist (priority-map source 0)
         prev {}]
    (let [[curr _] (min-dist unexplored dist)
          unexplored (disj unexplored curr)]
      (if (= target curr)
        (dist target)
        (let [neighbors (unexplored-neighbors graph unexplored curr)
              [dist prev] (check-neighbors graph neighbors dist prev curr)]
          (recur unexplored dist prev))))))

(defn shortest-path [graph]
  (let [max (dec (count graph))]
    (dijkstra graph [0 0] [max max])))

(defn add-risk [a b]
  (let [c (+ a b)]
    (if (< c 10)
      c
      ; this will break if c > 18
      (- c 9))))

(defn embiggen-row [row]
  (-> (for [n (range 5)]
        (map #(add-risk % n) row))
      flatten
      vec))

(defn embiggen [graph]
  (vec (for [n (range 5)
        r graph]
    (-> (map #(add-risk % n) r)
        embiggen-row))))

(let [input (read-input "input.txt")]
  (println "Part one:" (shortest-path input))
  (println "Part two:" (shortest-path (embiggen input))))