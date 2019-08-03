$(function() {

	/**
	 * 自定义系统全局js对象
	 */
	systemJs = {};

	/**
	 * 获取键盘按键
	 * @param {Object} callBack 回调函数
	 */
	systemJs.onkeydown = function(callBack) {

		$("body").keydown(function(event) {

			/*keyCode=13是回车键*/
			if (event.keyCode == "13") {
				callBack();
			}

		});
	}

	
	/**验证码*/
	systemJs.getCode = function(){
		$.ajax({
			url: '/setCode'
			,type: 'get'
			,dataType: 'text'
			,success: function(data){
				$('#code').val(data);
			}
		});
	}

	/*全局ajax对象*/

	/**
	 * @param {Object} url 请求路径
	 * @param {Object} dataType 返回的数据格式【josn, html】
	 * @param {Object} data 请求参数
	 * @param {Object} type 请求方式【POST, GET】
	 * @param {Object} params 拓展参数，非必填
	 * @param {Object} callBack 请求成功，回调函数
	 */
	systemJs.ajax = function(url, dataType, type, data, callBack, params) {
		var res = false;

		$.ajax({
			url: url,
			async: true,
			data: data,
			type: type,
			dataType: dataType,
			beforeSend: function() {
				/*请求前*/
				layer.load(1, {
					time: 3 * 1000,
					shade: 0.3
				});
			},
			success: function(data) {
				/*请求成功*/
				if(null == data){
					return;
				}
				if(data.code == -1){
					systemJs.errorLayer(data.message);
				}else if(data.code == 0){
					/**成功*/
					systemJs.okLayer(data.message);
					if(typeof(callBack) != 'undefined'){
						callBack();
					}
				}else{
					return;
				}
			},
			complete: function(data) {
				/*请求完成*/
			},
			error: function(data) {
				/*请求出错*/
				systemJs.errorLayer('请求异常');
			}
		});

		return res;
	}

	systemJs.attrInput = function() {

		$("input.layui-input").attr("lay-verType", "tips");

	}

	systemJs.attrInput();


/**
 * 系统通用输入层
 * @param {Object} type
 * @param {Object} formType 输入框类型，支持0（文本）默认1（密码）2（多行文本）
 * @param {Object} defaultValue 初始时的值，默认空字符
 * @param {Object} title 标题
 * @param {Object} width 宽度，以px为单位
 * @param {Object} height 高度
 * @param {Object} maxlength 可输入文本的最大长度，默认500
 * @param {Object} callBack 回调函数
 */
	systemJs.prompt = function(formType, defaultValue, title, width, height, maxlength, callBack) {
		
		if(typeof(formType) == 'undefined' || null == formType){ formType = 1}
		if(typeof(defaultValue) == 'undefined' || null == defaultValue){ defaultValue = ''}
		if(typeof(title) == 'undefined' || null == title){ title = '信息'}
		if(typeof(width) == 'undefined' || null == width){ width = '100px'}
		if(typeof(height) == 'undefined' || null == height){ height = '50px'}
		if(typeof(maxlength) == 'undefined' || null == maxlength){ maxlength = 500}
		if(typeof(callBack) == 'undefined'){ callBack = function(){}}
		
		layer.prompt({
			formType: formType,
			value: defaultValue,
			title: title,
			area: [width, height],
			maxlength: 100
		}, function(value, index, elem) {
			console.log(value);
			//alert(value); //得到value
			callBack(value);
			layer.close(index);
		});

	}

	systemJs.errorLayer = function(msg){
		layer.msg(msg, {
			icon: 5,
			anim: 6
		});
	}
	
	systemJs.okLayer = function(msg){
		layer.msg(msg, {
			icon: 6,
		});
	}
	
	systemJs.layerLoad = function(){
		layer.load(1, {
			time: 3 * 1000,
			shade: 0.3
		});
	}
	
})
