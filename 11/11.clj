(ns aoc.11
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-line [line]
  (mapv #(-> % str Integer/parseInt) line))

(defn read-input [f]
  (->> (slurp f)
       str/split-lines
       (mapv parse-line)))

(defn neighbor-coords [[y x]]
  (for [dx (range -1 2)
        dy (range -1 2)
        :when (not= dx dy 0) ]
    [(+ y dy) (+ x dx)]))

(defn neighbors [y x v]
  (->> (neighbor-coords [y x])
     (map #(get-in v %))
     (filter (complement nil?))))

(defn tick [row]
  (mapv inc row))

(defn not-in? [coll k]
  (not (contains? coll k)))

(defn flash-indicies [grid flashed]
  (for [y (range (count grid))
        x (range (count (first grid)))
        :when (and 
                (-> (get-in grid [y x]) (> 9))
                (not-in? flashed [y x]))]
     [y x]))

(defn propagate-flashes [grid fneighbors]
  (if (empty? fneighbors)
    grid
    (recur
      (update-in grid (first fneighbors) inc)
      (rest fneighbors))))

(defn filter-invalid [grid coords]
  (filter #(-> (get-in grid %) nil? not) coords))

(defn flash-cascade [grid]
  (loop [grid grid
         flashed #{}]
    (if-let [flashes (-> (flash-indicies grid flashed) seq)]
      (recur
        (propagate-flashes grid (->> (mapcat neighbor-coords flashes) (filter-invalid grid)))
        (reduce conj flashed flashes))
      [grid, (count flashed)])))

(defn clamp [row]
  (mapv #(if (> % 9) 0 %) row))

(defn step [grid]
  (let [[grid, fcount]
        (->> grid
            (mapv tick)
            (flash-cascade))]
    [(mapv clamp grid), fcount]))

(defn part-one [grid]
  (loop [grid grid
         fcount 0
         iter 0]
    (if (>= iter 100)
      fcount
      (let [[ngrid, nfcount] (step grid)]
        (recur
          ngrid
          (+ nfcount fcount)
          (inc iter))))))

(defn grid-size [grid]
  (* (count (first grid))
     (count grid)))

(defn part-two [grid]
  (loop [grid grid
         fcount 0
         iter 0]
    (if (= fcount (grid-size grid))
      iter
      (let [[ngrid, nfcount] (step grid)]
        (recur
          ngrid
          nfcount
          (inc iter))))))

(println "Part one:" (part-one (read-input "input.txt")))
(println "Part two:" (part-two (read-input "input.txt")))