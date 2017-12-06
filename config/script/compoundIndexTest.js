


print("!!! creating compound Index");
db.getSiblingDB('5915CD936A7F240007000081_db').users2.ensureIndex({email:1,createTime:1},{name:'index_test'});

/*var createCompoundIndex = function(db, callback) {
    // Get the documents collection
    var collection = db.collection('users');
    // Create the index
    collection.createIndex(
        { lastName : -1, dateOfBirth : 1 }, function(err, result) {
            console.log(result);
            callback(result);
        });
};*/
