package com.witcos.entity.task;

public class Surface {
	 
	private String id;   		//版块编号
	private String sid; 		//样式编号
	private String type;  		//类型
	private Integer x;			//x坐标
	private Integer y;			//y坐标
	private Integer width;		//宽度
	private Integer height;		//高度
	private Integer index;		//图层排序     值越小 则靠后
	private Integer isMain;		//是否是主播放区     1主 0辅
	
	private String txt;			//静态文本区内容
	private String alpha;		//透明度
	private String font;		//字体
	private String fontSize;	//字号大小
	private String backColor;	//背景颜色
	private String backAlpha;	//背景透明度
	private String foreColor;	//前景颜色
	private String foreAlpha;	//前景透明度
	
	private String backPic;		//背景图片
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}
	
	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getBackAlpha() {
		return backAlpha;
	}

	public void setBackAlpha(String backAlpha) {
		this.backAlpha = backAlpha;
	}

	public String getForeColor() {
		return foreColor;
	}

	public void setForeColor(String foreColor) {
		this.foreColor = foreColor;
	}

	public String getForeAlpha() {
		return foreAlpha;
	}

	public void setForeAlpha(String foreAlpha) {
		this.foreAlpha = foreAlpha;
	}

	public String getBackPic() {
		return backPic;
	}

	public void setBackPic(String backPic) {
		this.backPic = backPic;
	}
	
}
