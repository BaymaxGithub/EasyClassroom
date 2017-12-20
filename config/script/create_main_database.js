


print("!!! creating main database");
db.getSiblingDB('59632C4C6A7F2400070006F5_db').users.ensureIndex({_id:1});
//调用接口传参时使用小写格式59632c4c6a7f2400070006f5


/*
var coll=db.users;
print("!!!insert admin");
coll.update({email: 'admin@admin.com',oid: '59632c4c6a7f2400070006f5',password : "admin"}, true, false);
*/
