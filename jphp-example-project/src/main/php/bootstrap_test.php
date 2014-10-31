<?php

use php\time\Time;

$foo = new Foobar([
    [Time::now(), Time::now()],
    [Time::today(), Time::today()]
]);
