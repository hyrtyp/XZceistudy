package com.hyrt.cei.vo;

import android.view.View;
import android.widget.*;
import java.io.Serializable;

public class PreloadContolGroup implements Serializable {

	private static final long serialVersionUID = 0x9586ab5a58c60d1dL;
	private RelativeLayout rootRelativeLayout;
	private ProgressBar ProgressBarDown;
	private Button btnControl;
	private Button btnDelete;
	private Button btnPlay;
	private Button btnAddCourse;
    private View reloadView;
	private TextView lblPercent;
	private TextView lblContent;
	private RelativeLayout linearLayProcess;
	private LinearLayout linearLayProcessStatus;
	private String playId;

    public View getReloadView() {
        return reloadView;
    }

    public void setReloadView(View reloadView) {
        this.reloadView = reloadView;
    }

    public Button getBtnAddCourse() {
		return btnAddCourse;
	}

	public void setBtnAddCourse(Button btnAddCourse) {
		this.btnAddCourse = btnAddCourse;
	}

	public TextView getLblContent() {
		return lblContent;
	}

	public void setLblContent(TextView lblContent) {
		this.lblContent = lblContent;
	}

	public PreloadContolGroup() {
	}

	public Button getBtnControl() {
		return btnControl;
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public Button getBtnPlay() {
		return btnPlay;
	}

	public TextView getLblPercent() {
		return lblPercent;
	}

	public RelativeLayout getLinearLayProcess() {
		return linearLayProcess;
	}

	public LinearLayout getLinearLayProcessStatus() {
		return linearLayProcessStatus;
	}

	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public ProgressBar getProgressBarDown() {
		return ProgressBarDown;
	}

	public void setBtnControl(Button button) {
		btnControl = button;
	}

	public void setBtnDelete(Button button) {
		btnDelete = button;
	}

	public void setBtnPlay(Button button) {
		btnPlay = button;
	}

	public void setLblPercent(TextView textview) {
		lblPercent = textview;
	}

	public void setLinearLayProcess(RelativeLayout linearlayout) {
		linearLayProcess = linearlayout;
	}

	public void setLinearLayProcessStatus(LinearLayout linearlayout) {
		linearLayProcessStatus = linearlayout;
	}

	public void setProgressBarDown(ProgressBar progressbar) {
		ProgressBarDown = progressbar;
	}

	public RelativeLayout getRootRelativeLayout() {
		return rootRelativeLayout;
	}

	public void setRootRelativeLayout(RelativeLayout rootRelativeLayout) {
		this.rootRelativeLayout = rootRelativeLayout;
	}
}
