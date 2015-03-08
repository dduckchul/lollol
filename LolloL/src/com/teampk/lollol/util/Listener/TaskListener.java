package com.teampk.lollol.util.Listener;

import com.teampk.lollol.dto.MemberDTO;

public interface TaskListener {

	public void onTaskComplete(MemberDTO member);
	public void onTaskComplete(boolean result);
	public void onTaskComplete(String result);
}
