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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mabe02
 */
public class CommonProfile extends KeyMappingProfile
{
    private static final List<CharacterPattern> COMMON_PATTERNS =
            new ArrayList<CharacterPattern>(Arrays.asList(
                new CharacterPattern[] {
                    new CharacterPattern(new Key(Key.Kind.ArrowUp), ESC_CODE, '[', 'A'),
                    new CharacterPattern(new Key(Key.Kind.ArrowDown), ESC_CODE, '[', 'B'),
                    new CharacterPattern(new Key(Key.Kind.ArrowRight), ESC_CODE, '[', 'C'),
                    new CharacterPattern(new Key(Key.Kind.ArrowLeft), ESC_CODE, '[', 'D'),
                    new CharacterPattern(new Key(Key.Kind.Tab), '\t'),
                    new CharacterPattern(new Key(Key.Kind.Enter), '\n'),
                    new CharacterPattern(new Key(Key.Kind.ReverseTab), ESC_CODE, '[', 'Z'),
                    new CharacterPattern(new Key(Key.Kind.Backspace), (char)0x7f),
                    new CharacterPattern(new Key(Key.Kind.Insert), ESC_CODE, '[', '2', '~'),
                    new CharacterPattern(new Key(Key.Kind.Delete), ESC_CODE, '[', '3', '~'),
                    new CharacterPattern(new Key(Key.Kind.Home), ESC_CODE, '[', 'H'),
                    new CharacterPattern(new Key(Key.Kind.End), ESC_CODE, '[', 'F'),
                    new CharacterPattern(new Key(Key.Kind.PageUp), ESC_CODE, '[', '5', '~'),
                    new CharacterPattern(new Key(Key.Kind.PageDown), ESC_CODE, '[', '6', '~')
                }));

    @Override
    Collection<CharacterPattern> getPatterns()
    {
        return new ArrayList<CharacterPattern>(COMMON_PATTERNS);
    }

}
