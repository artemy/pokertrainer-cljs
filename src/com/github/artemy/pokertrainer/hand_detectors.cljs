(ns com.github.artemy.pokertrainer.hand-detectors)

(declare straight-flush?)
(declare d-flush?)
(declare straight?)
(declare three-of-a-kind?)
(declare pair?)

;-- Utils
(defn group-by-rank [cards] (group-by :rank cards))

(defn group-by-suit [cards] (group-by :suit cards))

(defn sort-by-rank [cards] (sort-by :rank cards))

(defn find-groups-by-rank-with-size
  [cards size]
  (let [groups (vals (group-by-rank cards))]
    (filter #(= size (count %)) groups)))

(defn has-groups-with-size?
  [cards size]
  (some #(< 1 (count %)) (find-groups-by-rank-with-size cards size)))

(defn count-pairs [cards]
  (count (find-groups-by-rank-with-size cards 2)))

;-- analyzers
(defn royal-flush?
  [cards]
  (and
    (straight-flush? cards)
    (= 10 (:rank (first (sort-by-rank cards))))))

(defn straight-flush?
  [cards]
  (and
    (straight? cards)
    (d-flush? cards)))

(defn four-of-a-kind?
  [cards]
  (has-groups-with-size? cards 4))

(defn full-house?
  [cards]
  (and
    (three-of-a-kind? cards)
    (pair? cards)))

(defn d-flush?
  [cards]
  (some #(<= 5 (count %)) (vals (group-by-suit cards))))

(defn straight?
  [cards]
  (let [sorted-ranks (reverse (distinct (map :rank (sort-by-rank cards))))
        cycled-sorted-ranks (take (+ 1 (count sorted-ranks)) (cycle sorted-ranks))
        all-permutations-of-sorted-ranks (partition 5 1 cycled-sorted-ranks)
        calculate-differences (fn [m] (map (fn [[i j]] (- i j)) (partition 2 1 m)))
        allowed-difference? (fn [d] (every? (fn [diff] (or (= 1 diff) (= -12 diff))) d))]
    (some allowed-difference? (map calculate-differences all-permutations-of-sorted-ranks))))

(defn three-of-a-kind?
  [cards]
  (has-groups-with-size? cards 3))

(defn two-pairs?
  [cards]
  (= 2 (count-pairs cards)))

(defn pair?
  [cards]
  (= 1 (count-pairs cards)))

(defn high-card?
  [ignore] true)

(def hand-analyzers
  (array-map :royalFlush royal-flush?
             :straightFlush straight-flush?
             :fourOfAKind four-of-a-kind?
             :fullHouse full-house?
             :flush d-flush?
             :straight straight?
             :threeOfAKind three-of-a-kind?
             :twoPairs two-pairs?
             :pair pair?
             :highCard high-card?))

(defn compute-correct-answer
  [cards]
  (first (for [[k v] hand-analyzers
               :when (v cards)] k)))

(defn correct-answer?
  [answer cards]
  (let [correct-answer (compute-correct-answer cards)]
    (= answer correct-answer)))
