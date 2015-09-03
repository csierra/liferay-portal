/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.shopping.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.PartitionableModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the ShoppingItemField service. Represents a row in the &quot;ShoppingItemField&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.shopping.model.impl.ShoppingItemFieldModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.shopping.model.impl.ShoppingItemFieldImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ShoppingItemField
 * @see com.liferay.shopping.model.impl.ShoppingItemFieldImpl
 * @see com.liferay.shopping.model.impl.ShoppingItemFieldModelImpl
 * @generated
 */
@ProviderType
public interface ShoppingItemFieldModel extends BaseModel<ShoppingItemField>,
	PartitionableModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a shopping item field model instance should use the {@link ShoppingItemField} interface instead.
	 */

	/**
	 * Returns the primary key of this shopping item field.
	 *
	 * @return the primary key of this shopping item field
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this shopping item field.
	 *
	 * @param primaryKey the primary key of this shopping item field
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the item field ID of this shopping item field.
	 *
	 * @return the item field ID of this shopping item field
	 */
	public long getItemFieldId();

	/**
	 * Sets the item field ID of this shopping item field.
	 *
	 * @param itemFieldId the item field ID of this shopping item field
	 */
	public void setItemFieldId(long itemFieldId);

	/**
	 * Returns the item ID of this shopping item field.
	 *
	 * @return the item ID of this shopping item field
	 */
	public long getItemId();

	/**
	 * Sets the item ID of this shopping item field.
	 *
	 * @param itemId the item ID of this shopping item field
	 */
	public void setItemId(long itemId);

	/**
	 * Returns the name of this shopping item field.
	 *
	 * @return the name of this shopping item field
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this shopping item field.
	 *
	 * @param name the name of this shopping item field
	 */
	public void setName(String name);

	/**
	 * Returns the values of this shopping item field.
	 *
	 * @return the values of this shopping item field
	 */
	@AutoEscape
	public String getValues();

	/**
	 * Sets the values of this shopping item field.
	 *
	 * @param values the values of this shopping item field
	 */
	public void setValues(String values);

	/**
	 * Returns the description of this shopping item field.
	 *
	 * @return the description of this shopping item field
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this shopping item field.
	 *
	 * @param description the description of this shopping item field
	 */
	public void setDescription(String description);

	/**
	 * Returns the company ID of this shopping item field.
	 *
	 * @return the company ID of this shopping item field
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this shopping item field.
	 *
	 * @param companyId the company ID of this shopping item field
	 */
	@Override
	public void setCompanyId(long companyId);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(
		com.liferay.shopping.model.ShoppingItemField shoppingItemField);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.shopping.model.ShoppingItemField> toCacheModel();

	@Override
	public com.liferay.shopping.model.ShoppingItemField toEscapedModel();

	@Override
	public com.liferay.shopping.model.ShoppingItemField toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}