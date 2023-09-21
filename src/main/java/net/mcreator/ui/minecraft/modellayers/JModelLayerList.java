/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2012-2020, Pylo
 * Copyright (C) 2020-2023, Pylo, opensource contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.mcreator.ui.minecraft.modellayers;

import net.mcreator.element.types.LivingEntity;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.JEntriesList;
import net.mcreator.ui.validation.AggregatedValidationResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JModelLayerList extends JEntriesList {

	private final List<JModelLayerListEntry> entryList = new ArrayList<>();

	private final JPanel entries = new JPanel(new GridLayout(0, 1, 5, 5));

	public JModelLayerList(MCreator mcreator, IHelpContext gui) {
		super(mcreator, new BorderLayout(), gui);
		setOpaque(false);

		JPanel topbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topbar.setBackground((Color) UIManager.get("MCreatorLAF.LIGHT_ACCENT"));

		add.setText(L10N.t("elementgui.living_entity.add_model_layer"));
		topbar.add(add);

		add("North", topbar);

		entries.setOpaque(false);

		add.addActionListener(e -> {
			JModelLayerListEntry entry = new JModelLayerListEntry(mcreator, gui, entries, entryList);
			registerEntryUI(entry);
		});

		add("Center", PanelUtils.pullElementUp(entries));

		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
				L10N.t("elementgui.living_entity.model_layers"), 0, 0, getFont().deriveFont(12.0f),
				(Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
	}

	@Override public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		add.setEnabled(false);
	}

	public void reloadDataLists() {
		entryList.forEach(JModelLayerListEntry::reloadDataLists);
	}

	public List<LivingEntity.ModelLayerEntry> getModelLayers() {
		return entryList.stream().map(JModelLayerListEntry::getEntry).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public void setModelLayers(List<LivingEntity.ModelLayerEntry> pool) {
		pool.forEach(e -> {
			JModelLayerListEntry entry = new JModelLayerListEntry(mcreator, gui, entries, entryList);
			registerEntryUI(entry);
			entry.setEntry(e);
		});
	}

	public AggregatedValidationResult getValidationResult() {
		AggregatedValidationResult validationResult = new AggregatedValidationResult();
		entryList.forEach(validationResult::addValidationElement);
		return validationResult;
	}

}
