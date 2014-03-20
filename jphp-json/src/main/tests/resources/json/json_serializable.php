--TEST--
Test JsonSerializable
--FILE--
<?php
class Foo implements JsonSerializable {
    public function jsonSerialize() {
        return ["x" => 20, "y" => 30];
    }
}

echo (json_encode(new Foo()));
?>
--EXPECTF--
{"x":20,"y":30}
