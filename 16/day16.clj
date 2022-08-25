(ns day16)

(def char->bits
 (-> {\0 "0000"
      \1 "0001"
      \2 "0010"
      \3 "0011"
      \4 "0100"
      \5 "0101"
      \6 "0110"
      \7 "0111"
      \8 "1000"
      \9 "1001"
      \A "1010"
      \B "1011"
      \C "1100"
      \D "1101"
      \E "1110"
      \F "1111"}
  (update-vals seq)))

(defn parse [s]
  (-> (map char->bits s)
      flatten))

(defn bits->int [bits]
  (loop [bits (map #(Character/getNumericValue %) (reverse bits))
         x 1
         n 0]
    (if (empty? bits)
      n
      (recur
        (rest bits)
        (bit-shift-left x 1)
        (+ n (* (first bits) x))))))

(defn decode-type [t]
  (case t
    0 :sum
    1 :prod
    2 :min
    3 :max
    4 :lit
    5 :gt
    6 :lt
    7 :eq))

(defn read-header [bits]
  (if (< (count bits) 6)
    nil
    (let [[header rest] (split-at 6 bits)
          [v t] (map bits->int (split-at 3 header))]
      [v (decode-type t) rest])))

(defn decode-literal [bits]
  (loop [bits bits
         accum []]
    (let [[chunk rest] (split-at 5 bits)
          [[control] valbits] (split-at 1 chunk)]
      (if (= control \0)
        [(bits->int (into accum valbits))
         rest]
        (recur
         rest
         (into accum valbits))))))

(declare decode-packet)

(defn decode-subpackets-bits [bits n]
  (loop [bits bits
         n n
         packets []]
    (if (<= n 0)
      [packets bits]
      (let [b (count bits)
            [packet rest] (decode-packet bits)]
        (recur
         rest
         (- n (- b (count rest)))
         (conj packets packet))))))

(defn decode-subpackets-count [bits n]
  (loop [bits bits
         n n
         packets []]
    (if (<= n 0)
      [packets bits]
      (let [[packet rest] (decode-packet bits)]
        (recur
         rest
         (dec n)
         (conj packets packet))))))

(defn decode-subpackets [bits]
  (loop [bits bits
         packets []]
    (let [[[control] rest] (split-at 1 bits)]
      (case control
        \0 (let [[valbits rest] (split-at 15 rest)
                 n (bits->int valbits)]
             (decode-subpackets-bits rest n)) 
        \1 (let [[valbits rest] (split-at 11 rest)
                 n (bits->int valbits)]
             (decode-subpackets-count rest n))))))

(defn decode-packet [bits]
  (let [[v t rest] (read-header bits)
        packet {:v v :t t}]
    (case t
      nil nil
      :lit (let [[val rest] (decode-literal rest)]
             [(assoc packet :val val)
              rest])
      (let [[sub rest] (decode-subpackets rest)]
        [(assoc packet :sub sub)
         rest]))))

(defn version-sum [root]
  (->> (tree-seq
        #(contains? % :sub)
        #(get % :sub)
        root)
    (map :v)
    (apply +)))

(defn bool->int [b]
  (if b 1 0))

(defn evaluate [{t :t sub :sub :as p}]
  (if (= t :lit)
    (:val p)
    (let [sub (map evaluate sub)]
      (case t
        :sum (apply + sub)
        :prod (apply * sub)
        :min (apply min sub)
        :max (apply max sub)
        :lt (bool->int (apply < sub))
        :gt (bool->int (apply > sub))
        :eq (bool->int (apply = sub))))))

(def input (slurp "input.txt"))
(def root (first (decode-packet (parse input))))

(defn evaluate-str [s]
  (-> (parse s)
      decode-packet
      first
      evaluate))

(println "Part one:" (version-sum root))
(println "Part two:" (evaluate root))