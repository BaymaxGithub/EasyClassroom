/**
 * Created by 12801 on 2017/12/4.
 */



/*print("creating user admin");
db = db.getSiblingDB('admin');
db.dropUser('admin');
db.createUser({user: "admin", pwd: "admin", roles: ["root"]});*/

db = db.getSiblingDB('zhu_config');
print("creating user zhu_config");
db.dropUser("zhu_config");
db.createUser({user: "zhu_config", pwd: "zhu_config", roles: ["dbOwner"]});
var coll=db.V01;
coll.drop();

coll.update({name: 'public'}, {
    $set: {
        config: {
            "apiServerIP" : "127.0.0.1",
            "apiServerPort" : 80,
            "mongodbHost" : "127.0.0.1",
            "mongodbPort" : 27017,
            "mongodbUsername" : "zhu_config",
            "mongodbPassword" : "zhu_config"
        }
    }
}, true, false);

coll.update({name: 'easy_classroom'}, {
    $set: {
        config: {
            apiServerIP: 'nginx',
            apiServerPort: 80
        }
    }
}, true, false);





