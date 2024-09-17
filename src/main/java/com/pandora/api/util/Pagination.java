package com.pandora.api.util;

public class Pagination {
	
	int page;
	int pages;
	
	public Pagination()
	{
		page=0;
		pages=1;
	}
	public Pagination(int page, int pages)
	{
		setPage(page);
		setPages(pages);
		if(page>pages)
		{
			setPage(0);
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		if(pages>0)
		{
			this.pages = pages;
		}
		else
		{
			this.pages= 1;
		}
	}
	
	public int getMax()
	{
		int added=4-page+getMin();
		if(pages>page+added)
		{
			return page+added;
		}
		else
		{
			return pages;
		}
	}
	
	public int getMin()
	{
		if(page-2>0)
			return page-2;
		else
			return 1;
	}

}
