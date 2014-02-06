package com.adgsoftware.mydomo.engine.oldcontroller;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.adgsoftware.mydomo.engine.house.Label;

/**
 * LabelList represents the link between a controller and label s.<br>
 * This link is bi-directionnal since when you add a label to a controller
 * controller contains a new label.
 *
 */
public class LabelList implements List<Label> {
	
	private Controller<? extends Status> controller;
	private List<Label> labelList = new ArrayList<Label>();
	
	public LabelList(Controller<? extends Status> controller) {
		this.controller = controller;
	}
	
	/**
	 * Add a label to the controller and add the controller to the controller list of the label.
	 * @param label the label to associate
	 * @return true if association works fine
	 */
	public boolean add(Label label) {
		if (!labelList.contains(label)) { // To break the loop with Controller.add
			boolean result = labelList.add(label);
			label.add(controller);
			return result;
		} else {
			return false;
		}
	}
	
	public void add(int location, Label label) {
		if (!labelList.contains(label)) { // To break the loop with Controller.add
			labelList.add(location, label);
			label.add(location, controller);
		}
	}

	public boolean addAll(Collection<? extends Label> labelCollection) {
		if (labelList.addAll(labelCollection)) {
			for (Label label : labelCollection) {
				label.add(controller);
			}
			return true;
		}
		else {
			return false;
		}
	}

	public boolean addAll(int index, Collection<? extends Label> labelCollection) {
		if (labelList.addAll(index, labelCollection)) {
			for (Label label : labelCollection) {
				label.add(controller);
			}
			return true;
		} else {		
			return false;
		}
	}

	public void clear() {
		for (Label label : labelList) { // Break link
			label.remove(controller);
		}
		labelList.clear();
	}

	public boolean contains(Object object) {
		return labelList.contains(object);
	}

	public boolean containsAll(Collection<?> arg0) {
		return labelList.containsAll(arg0);
	}

	public Label get(int location) {
		return labelList.get(location);
	}

	public int indexOf(Object object) {
		return labelList.indexOf(object);
	}

	public boolean isEmpty() {
		return labelList.isEmpty();
	}

	public Iterator<Label> iterator() {
		return labelList.iterator();
	}

	public int lastIndexOf(Object object) {
		return labelList.lastIndexOf(object);
	}

	public ListIterator<Label> listIterator() {
		return labelList.listIterator();
	}

	public ListIterator<Label> listIterator(int location) {
		return labelList.listIterator(location);
	}

	public Label remove(int location) {
		// TODO consolidatation => avoid null pointer exception
		labelList.get(location).remove(controller); // TODO Break the bidirectionnal link
		return labelList.remove(location);
	}

	public boolean remove(Object object) {
		
		boolean result = false;
		if (labelList.contains(object)) { // Break the bidirectionnal link
			result = labelList.remove(object);
			((Label)object).remove(controller);
		}
		return result;
	}

	public boolean removeAll(Collection<?> labelCollection) {
		for (Object object : labelCollection) {
			if (object instanceof Label) {
				Label label = (Label) object;
				label.remove(controller);
			}
		}
		return labelList.removeAll(labelCollection);
	}

	public boolean retainAll(Collection<?> arg0) {
		return labelList.retainAll(arg0);
	}

	public Label set(int location, Label object) {
		labelList.get(location).remove(controller); // Remove the link of the previous controller
		object.add(controller);
		return labelList.set(location, object);
	}

	public int size() {
		return labelList.size();
	}

	public List<Label> subList(int start, int end) {
		return labelList.subList(start, end);
	}

	public Object[] toArray() {
		return labelList.toArray();
	}

	public <T> T[] toArray(T[] array) {
		return labelList.toArray(array);
	}
}
