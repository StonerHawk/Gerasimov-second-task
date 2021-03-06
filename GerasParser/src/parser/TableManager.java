package parser;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JTable;


public class TableManager 
{
	private MyTableModel tableModel = null;
	private JTable table = null;
	private volatile static TableManager instance;

	/** Returns singletone class instance */
	public static TableManager getInstance() {
		if (instance == null) {
			synchronized (TableManager.class) {
				if (instance == null) {
					instance = new TableManager();
				}
			}
		}
		return instance;
	}
		
	private TableManager()
	{
		this.table = new JTable();
	}
	
	//Считаем количество дубликатов
	private int getDuplicates(Vector<String> data, String key) 
	{		
		return Collections.frequency(data, key);
	}
	
	//Избавляемся от дубликатов
	private Vector<TableItem> getRidOfDuplicateElements(Vector<TableItem> tableData) 
	{
		Vector<TableItem> noDuplicates = new Vector<TableItem>();

		SortedSet<TableItem> mySet = new TreeSet<TableItem>(new TableItemComparator());
		mySet.addAll(tableData);
		
		noDuplicates.addAll(mySet);
		
		Vector<TableItem> result = new Vector<TableItem>();
		
		for (TableItem tableItem : noDuplicates) 
		{
			int[] positions = new int[tableItem.getDuplicates()];
			int iterator = 0;
			for (int i = 0; i < noDuplicates.size(); i++) 
			{
				if (tableItem.equals(noDuplicates.get(i))) 
				{
					positions[iterator] = i;
					iterator++;
				}
			}
			iterator = 0;
			
			TableItem targetItem = noDuplicates.get(positions[0]);
			
			if (!result.contains(targetItem))
			{
				result.add(targetItem);
			} 
			
		}
		
		return result;
	}
	
	//Задаем данные для таблицы
	public void setTableData(Vector<String> data)
	{
		Vector<TableItem> tableData = new Vector<TableItem>();
		
		for (String stringItem : data) 
		{
			TableItem tItem = new TableItem(stringItem, getDuplicates(data, stringItem));
			tableData.add(tItem);
		}
		tableData = getRidOfDuplicateElements(tableData);
		this.tableModel = new MyTableModel(tableData);
		this.table.setModel(tableModel);
	}
			
	public JTable getTable()
	{
		return table;
	}
}
