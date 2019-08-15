db.twowords.aggregate([
    {
        $group:{
            _id:{
                Word: "$word1"
            },
            entries: {$addToSet: "$word2"},
            count: {$sum: 1}
        }
    }, {
        $match: {
            count: {"$gt":1}
        }
    }, {
        $sort: {
            count: -1
        }
    }
]);