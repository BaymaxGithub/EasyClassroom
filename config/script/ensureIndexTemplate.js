 /**
 * Created by 12801 on 2017/12/5.
  * aggregate:管道操作
  * $match: 滤波操作，筛选符合条件文档，作为下一阶段的输入，如：
   { $match : { score : { $gt : 70, $lte : 90 } } }
  $match用于获取分数大于70小于或等于90记录，然后将符合条件的记录送到下一阶段的管道操作符进行处理。
//////////////////////////////////////////////////////////////
  *$group : 对数据进行分组
  * $group的时候必须要指定一个_id域，同时也可以包含一些算术类型的表达式操作符：
  注意： 1.$group的输出是无序的。
        2.$group操作目前是在内存中进行的，所以不能用它来对大量个数的文档进行分组。
 ///////////////////////////////////////////////////////////////////
  * $project: 数据投影，主要用于重命名、增加和删除字段，如：
      { $project : {
        title : 1 ,
        author : 1 ,
       }}
  这样的话结果中就只还有_id,tilte和author三个字段了，默认情况下_id字段是被包含的，如果要想不包含_id话可以这样_id : 0
  * $match：用于过滤数据，只输出符合条件的文档。$match使用MongoDB的标准查询操作。
 */

/**
 *
 * 所以：以下脚本的作用就是对ABCDE_db数据库中user_dbs表进行聚合操作（本质是查询）
 *  $match不过滤
 *  $group按照dbName键来分组（相同的dbName是同一组）
 *  $project 将原文档中的_id名称换成dbName  且不包含_id（不明写0的话默认会包含_id）
 */
function forEachDb(callback) {
    db.getSiblingDB('ABCDE_db').user_dbs.aggregate({
        $match: {}
    }, {
        $group: {_id: "$dbName"}
    }, {
        $project: {dbName: "$_id", _id: 0}
    }).forEach(function (result) {
        callback(db.getSiblingDB(result.dbName))
    });
}

forEachDb(function (db) {
    db.user.location.ensureIndex({uid: 1});
    db.user.location.ensureIndex({timestamp: 1},{ expireAfterSeconds: 60*60*24*90});
});

/**上面使用的是js的回调函数方式,即调用函数A时要传入一个函数B进去
 * d当A执行完之后会自动调用B函数
 *
 * 所以js执行时，首先在开始定义了一个forEachDb函数，要求传入一个函数进去（变量callback接收，js弱类型，可以用变量保存函数）
 * 在41行时执行函数forEachDb，传入一个function (db) {.....}函数进去
 * 然后聚合操作后会得到一个满足条件的文档s,forEach方法会变量其中每个文档，
 * 并且为每个文档执行forEach中的函数
 *
 * 定义主函数的时候，我们让代码先去执行callback()回调函数，但输出结果却是后输出回调函数的内容。这就说明了主函数不用等待回调函数执行完，
 * 可以接着执行自己的代码。所以一般回调函数都用在耗时操作上面。比如ajax请求，比如处理文件等。
 */