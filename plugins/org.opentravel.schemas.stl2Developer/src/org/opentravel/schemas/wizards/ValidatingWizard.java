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
package org.opentravel.schemas.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Agnieszka Janowska / Dave Hollander
 * 
 */
public abstract class ValidatingWizard extends Wizard implements Validatable {

    private FormValidator validator = new NullValidator();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.opentravel.schemas.wizards.Validatable#setValidator(org.opentravel.schemas.wizards.FormValidator)
     */
    @Override
    public void setValidator(final FormValidator validator) {
        this.validator = validator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opentravel.schemas.wizards.Validatable#getValidator()
     */
    @Override
    public FormValidator getValidator() {
        return validator;
    }

}