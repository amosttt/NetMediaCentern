package com.witcos.entity.task;

public class Surface {
	 
	private String id;   		//�����
	private String sid; 		//��ʽ���
	private String type;  		//����
	private Integer x;			//x����
	private Integer y;			//y����
	private Integer width;		//���
	private Integer height;		//�߶�
	private Integer index;		//ͼ������     ֵԽС �򿿺�
	private Integer isMain;		//�Ƿ�����������     1�� 0��
	
	private String txt;			//��̬�ı�������
	private String alpha;		//͸����
	private String font;		//����
	private String fontSize;	//�ֺŴ�С
	private String backColor;	//������ɫ
	private String backAlpha;	//����͸����
	private String foreColor;	//ǰ����ɫ
	private String foreAlpha;	//ǰ��͸����
	
	private String backPic;		//����ͼƬ
	
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
