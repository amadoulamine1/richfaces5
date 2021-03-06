RichFaces.QUnit.run(function() {
    module("richfaces-queue-clear");

    QUnit.testDone(function(context) {
        RichFaces.queue.clear();
    });
    
    // queue.clear
    test("RichFaces.queue.clear", function() {
        expect(3);
        
        var element = document.getElementById("testDiv");
        var event = {type:"testevent"};
        var options = {requestDelay:1000};
        var opts = {queueId:"myQueueClear", param:"value"};
        
        RichFaces.queue.setQueueOptions(opts.queueId, options);
        ok(RichFaces.queue.isEmpty(), "empty");
        jsf.ajax.request(element, event, opts);
        jsf.ajax.request(element, event, opts);
        jsf.ajax.request(element, event, opts);
        ok(!RichFaces.queue.isEmpty(), "not empty");
        RichFaces.queue.clear();
        ok(RichFaces.queue.isEmpty(), "empty");
    });

});