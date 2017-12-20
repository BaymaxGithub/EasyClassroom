/**
 * Created by 12801 on 2017/12/11.
 */

db.getSiblingDB('112233_db').test.ensureIndex({_id:1});
var coll=db.test;
coll.drop();
print("!!! insert numberofLong");
coll.update({name: 'test'}, {
    $set: {
        config: {
            test:1111,
            rt_alarmQueryDay:NumberLong(7)
        }
    }
}, true, false);
