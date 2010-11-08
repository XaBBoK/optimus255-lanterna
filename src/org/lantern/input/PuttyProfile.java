/*
 *  Copyright (C) 2010 mabe02
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lantern.input;

import java.util.Collection;

/**
 *
 * @author mabe02
 */
public class PuttyProfile extends CommonProfile
{
    @Override
    Collection<CharacterPattern> getPatterns()
    {
        Collection<CharacterPattern> xtermPatterns = super.getPatterns();
        xtermPatterns.add(new CharacterPattern(new Key(Key.Kind.Home), ESC_CODE, '[', '1', '~'));
        xtermPatterns.add(new CharacterPattern(new Key(Key.Kind.End), ESC_CODE, '[', '4', '~'));
        return xtermPatterns;
    }
}
