--TEST--
MongoDB basic
--FILE--
<?php

use mongo\MongoClient;
use php\lib\reflect;

$client = new MongoClient();
try {
    $db = $client->database("jphp-mongo-ext");
    $col = $db->collection("test");

    var_dump($col->count());

    $doc = $col->insertOne(['x' => 100, 'y' => 200]);
    var_dump(reflect::typeOf($doc['_id']));

    var_dump($col->count());

    $doc = $col->find(['x' => 100, 'y' => 200])->first();
    var_dump($doc['x'], $doc['y']);
} finally {
    $client->close();
}

?>
--EXPECT--
int(0)
string(14) "mongo\ObjectId"
int(1)
int(100)
int(200)