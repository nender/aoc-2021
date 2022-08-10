(ns day14
  (:require [clojure.string :as str]))

(defn split-on [pred coll]
  "Like split-with but does not include the delimiter in the second
  collection"
  [(take-while pred coll)
   (rest (drop-while pred coll))])

(defn parse-rules [rules]
  (->> (map #(str/split % #" -> ") rules)
       (map (fn [[pattern value]]
              [(seq pattern)
               (first value)]))
       (into {})))

(defn parse [filename]
  (->> (slurp filename)
       str/split-lines
       (split-on (complement empty?))
       (apply (fn [[polymer] rules]
         [(seq polymer)
          (parse-rules rules)]))))

(defn evaluate-pair [rules pair]
  (if-let [value (rules pair)]
    [(first pair) value]
    (if (nil? (second pair))
      (first pair))))

(defn evaluate-rules-once [polymer rules]
  (->> (partition 2 1 (repeat nil) polymer)
       (map #(evaluate-pair rules %))
       flatten))

(defn evaluate-rules [[polymer rules] n]
  (loop [polymer polymer
         n n]
    (if (= 0 n)
      polymer
      (recur
        (evaluate-rules-once polymer rules)
        (dec n)))))

(def input (parse "input.txt"))
(println "Part one:"
  (let [freq (sort (map second (frequencies (evaluate-rules input 10))))]
    (- (last freq) (first freq))))