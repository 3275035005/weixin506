## 2.0.2（2023-06-19）
优化使用说明
#### 使用方法

```使用方法

<!-- flist:第一级数组 slist：第二级数组 tlist：第三级数组 @click：点击事件 注意：下一级pid与上一级id对应关联 -->
		
<cc-categorizeView :flist="flist" :slist="slist" :tlist="tlist" @click="navToList"></cc-categorizeView>
	

```

#### HTML代码实现部分
```html


<template>
	<view class="content">
		<!-- flist:第一级数组 slist：第二级数组 tlist：第三级数组 @click：点击事件 注意：下一级pid与上一级id对应关联 -->
		<cc-categorizeView :flist="flist" :slist="slist" :tlist="tlist" @click="navToList"></cc-categorizeView>
	
	</view>
</template>

<script>
	export default {
		
		data() {
			return {
				
				flist: [],
				slist: [],
				tlist: [],
			}
		},
		mounted() {
			// this.loadData();
			this.flist = [{
					id: 1,
					name: '手机数码'
				},
				{
					id: 2,
					name: '礼品鲜花'
				}
			];

			//pid为父级id,  //没有图的是2级分类
			this.slist = [{
					id: 5,
					pid: 1,
					name: '手机通讯'
				},
				{
					id: 6,
					pid: 1,
					name: '运营商'
				}, {
					id: 17,
					pid: 2,
					name: '礼品',
				},
				{
					id: 18,
					pid: 2,
					name: '鲜花',
				},
			];

			//3级分类 pid为父层级对应id
			this.tlist = [{
					id: 8,
					pid: 5,
					name: '全面屏手机',
					picture: 'https://cdn.pixabay.com/photo/2014/08/05/10/30/iphone-410324_1280.jpg'
				},
				{
					id: 9,
					pid: 5,
					name: '游戏手机',
					picture: 'https://cdn.pixabay.com/photo/2016/12/09/11/33/smartphone-1894723_1280.jpg'
				},
				{
					id: 10,
					pid: 5,
					name: '老人机',
					picture: '/static/temp/cate1.jpg'
				},
				{
					id: 11,
					pid: 5,
					name: '拍照手机',
					picture: 'https://cdn.pixabay.com/photo/2014/08/05/10/27/iphone-410311_1280.jpg'
				},
				{
					id: 12,
					pid: 5,
					name: '女性手机',
					picture: '/static/temp/cate5.jpg'
				},
				{
					id: 14,
					pid: 6,
					name: '合约机',
					picture: 'https://cdn.pixabay.com/photo/2014/08/05/10/27/iphone-410311_1280.jpg'
				},
				{
					id: 15,
					pid: 6,
					name: '选好卡',
					picture: '/static/temp/cate4.jpg'
				},
				{
					id: 16,
					pid: 6,
					name: '办套餐',
					picture: '/static/temp/cate5.jpg'
				},
				{
					id: 19,
					pid: 17,
					name: '风水摆件',
					picture: '/static/temp/cate7.jpg'
				},
				{
					id: 20,
					pid: 17,
					name: '创意礼品',
					picture: '/static/temp/cate8.jpg'
				},
				{
					id: 21,
					pid: 18,
					name: '鲜花',
					picture: '/static/temp/cate9.jpg'
				},
				{
					id: 22,
					pid: 18,
					name: '每周一花',
					picture: '/static/temp/cate10.jpg'
				},
				{
					id: 23,
					pid: 18,
					name: '卡通花束',
					picture: '/static/temp/cate11.jpg'
				},
				{
					id: 24,
					pid: 18,
					name: '永生花',
					picture: '/static/temp/cate12.jpg'
				},
			];


		},
		methods: {
			
			navToList(item) {

				uni.showModal({
					title: '温馨提示',
					content: '点击条目携带信息 = ' + JSON.stringify(item)
				})
			}
		}
	}
</script>

<style lang='scss'>
	page,
	.content {
		height: 100%;
		background-color: #f8f8f8;
	}

</style>




```

