~(function (win) {
    var xmUtils = {
        /**
         * 小数格式化
         * 使用方法 xmUtils.floatFormatter(123.222,2,true)
         * @param {*} src 需要格式化的小数
         * @param {*} pos 小数最多保留的位数
         * @param {*} flag 小数位数不足是否补0
         */
        floatFormatter: function (src, pos, flag) {
            if (pos == undefined || pos < 0) {
                pos = 2;
            }
            if (isNaN(src)) {
                return '';
            }
            var f = Math.round(src * Math.pow(10, pos)) / Math.pow(10, pos);
            var s = f.toString();
            if (pos && pos > 0) {
                var rs = s.indexOf('.');
                if (rs < 0) {
                    rs = s.length;
                    s += '.';
                }
                if (flag) {
                    while (s.length <= rs + pos) {
                        s += '0';
                    }
                }
            }
            return s;
        },
        /**
         * json对象自定义排序，原对象排好序
         * 实际上指定desc才倒序，其他的都是正序的
         * 使用 arrayObj.sort(function (a, b) {
                    return xmUtils.sortByProps(a, b, { "propA": "asc", "propB": "desc" });
                });
         * @param {*} item1 前一个对象
         * @param {*} item2 后一个对象
         * @param {*} obj   需要比较的属性及排序方式
         */
        sortByProps: function (item1, item2, obj) {
            var props = [];
            if (obj) {
                props.push(obj)
            }
            var cps = []; // 存储排序属性比较结果。
            // 如果未指定排序属性(即obj不存在)，则按照全属性升序排序。
            // 记录下两个排序项按照各个排序属性进行比较得到的结果
            var desc = false;
            if (props.length < 1) {
                for (var p in item1) {
                    if (item1[p] > item2[p]) {
                        cps.push(1);
                        break; // 大于时跳出循环。
                    } else if (item1[p] === item2[p]) {
                        cps.push(0);
                    } else {
                        cps.push(-1);
                        break; // 小于时跳出循环。
                    }
                }
            } else {
                for (var i = 0; i < props.length; i++) {
                    var prop = props[i];
                    for (var o in prop) {
                        desc = prop[o] === "desc";
                        if (item1[o] > item2[o]) {
                            cps.push(desc ? -1 : 1);
                            break; // 大于时跳出循环。
                        } else if (item1[o] === item2[o]) {
                            cps.push(0);
                        } else {
                            cps.push(desc ? 1 : -1);
                            break; // 小于时跳出循环。
                        }
                    }
                }
            }
            // 根据各排序属性比较结果综合判断得出两个比较项的最终大小关系
            for (var j = 0; j < cps.length; j++) {
                if (cps[j] === 1 || cps[j] === -1) {
                    return cps[j];
                }
            }
            return false;
        },
    }
    win.xmUtils = xmUtils;
})(window);