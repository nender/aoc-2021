(ns day14
  (:require [clojure.string :as str]))

(defn split-on [pred coll]
  "Like split-with but does not include the delimiter in the second
  collection"
  [(take-while pred coll)
   (rest (drop-while pred coll))])

(defn parse-rule [[pattern value]]
  (let [charval (first value)]
    [(seq pattern)
     [[(first pattern) charval]
      [charval (last pattern)]]]))

(defn parse-rules [rules]
  (->> (map #(str/split % #" -> ") rules)
       (map parse-rule)
       (into {})))

(defn parse [filename]
  (->> (slurp filename)
       str/split-lines
       (split-on (complement empty?))
       (apply (fn [[polymer] rules]
         [(seq polymer)
          (parse-rules rules)]))))

(defn evaluate-rule [[pair count] rules]
  (if-let [[pair1 pair2] (rules pair)]
    [[pair1 count] [pair2 count]]
    [pair count]))

(defn update-count [coll [k v]]
  (if-let [count (coll k)]
    (assoc coll k (+ v count))
    (assoc coll k v)))

(defn evaluate-rules-once [polymer rules]
  (->> (map #(evaluate-rule % rules) polymer)
       (apply concat)
       (reduce update-count {})))

(defn evaluate-rules [[polymer rules] n]
  (loop [polymer (frequencies (partition 2 1 polymer))
         n n]
    (if (= 0 n)
      polymer
      (recur
        (evaluate-rules-once polymer rules)
        (dec n)))))

(defn char-frequencies [l hist]
  "Given last character and pair frequencies, compute single char frequencies."
  (-> (->> (for [char (-> hist keys flatten set)]
              ;; to avoid double-counting, only sum counts where char is _first_
              ;; in pair key.
              ;; NOTE: I cheated and looked up this trick, was stumped how to go
              ;; from pair-frequencies to char-frequencies.
              (let [keys (filter #(= char (first %)) (keys hist))
                    count (apply + (map hist keys))]
                [char count]))
            flatten
            (apply hash-map))
      ;; because we skipped counting final char above, increment it by one
      (update l inc)))

(defn solve [[polymer :as input] n]
  (let [last-char (last polymer)
        hist (char-frequencies last-char (evaluate-rules input n))
        freq (sort (map second hist))]
    (- (last freq) (first freq))))

(let [input (parse "input.txt")]
  (println "Part one:" (solve input 10))
  (println "Part two:" (solve input 40)))