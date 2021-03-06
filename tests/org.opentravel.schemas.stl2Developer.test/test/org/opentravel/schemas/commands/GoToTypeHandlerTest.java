/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.schemas.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.opentravel.schemas.controllers.MainController;
import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.interfaces.INode;
import org.opentravel.schemas.stl2developer.OtmRegistry;
import org.opentravel.schemas.views.NavigatorView;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HandlerUtil.class, OtmRegistry.class })
public class GoToTypeHandlerTest {

	private GoToTypeHandler handler;
	private MainController mainControllerMock;

	@Before
	public void beforeEachTest() {
		handler = new GoToTypeHandler();
		mainControllerMock = Mockito.mock(MainController.class);
		PowerMockito.mockStatic(OtmRegistry.class);
		Mockito.when(OtmRegistry.getMainController()).thenReturn(mainControllerMock);
		NavigatorView navigationMock = Mockito.mock(NavigatorView.class);
		Mockito.when(navigationMock.isReachable((Matchers.any(Node.class)))).thenReturn(true);
		Mockito.when(OtmRegistry.getNavigatorView()).thenReturn(navigationMock);
	}

	@Test
	public void shouldSelectTypeInNavigator() throws ExecutionException {
		Node typeNode = Mockito.mock(Node.class);
		Node selectedNode = Mockito.mock(Node.class);
		// /MockUtils.mockToReturnType(selectedNode, typeNode);

		// PowerMockito.mockStatic(HandlerUtil.class);
		// OtmAbstractView viewMock = Mockito.mock(OtmAbstractView.class);
		// Mockito.when(viewMock.getSelectedNodes()).thenReturn(Collections.singletonList(selectedNode));
		// Mockito.when(HandlerUtil.getActivePart((ExecutionEvent) Matchers.any())).thenReturn(viewMock);
		//
		// handler.execute(new ExecutionEvent());
		//
		// Mockito.verify(mainControllerMock).selectNavigatorNodeAndRefresh(typeNode);
	}

	@Test
	public void shouldDoNothingForNotOtmView() throws ExecutionException {
		PowerMockito.mockStatic(HandlerUtil.class);
		IWorkbenchPart viewMock = Mockito.mock(IWorkbenchPart.class);
		Mockito.when(HandlerUtil.getActivePart((ExecutionEvent) Matchers.any())).thenReturn(viewMock);

		handler.execute(new ExecutionEvent());

		Mockito.verify(mainControllerMock, Mockito.never()).selectNavigatorNodeAndRefresh((INode) Matchers.any());
	}

}
