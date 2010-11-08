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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.lantern.LanternException;

/**
 *
 * @author mabe02
 */
public class InputDecoder
{
    private final Reader source;
    private final Queue<Character> inputBuffer;
    private final Queue<Character> leftOverQueue;
    private final Set<CharacterPattern> bytePatterns;
    private final List<Character> currentMatching;

    public InputDecoder(final Reader source)
    {
        this.source = source;
        this.inputBuffer = new LinkedList<Character>();
        this.leftOverQueue = new LinkedList<Character>();
        this.bytePatterns = new HashSet<CharacterPattern>();
        this.currentMatching = new ArrayList<Character>();
    }

    public InputDecoder(final Reader source, final KeyMappingProfile profile)
    {
        this(source);
        addProfile(profile);
    }

    public void addProfile(KeyMappingProfile profile)
    {
        for(CharacterPattern pattern: profile.getPatterns())
            bytePatterns.add(pattern);
    }

    public Key getNextCharacter() throws LanternException
    {
        if(leftOverQueue.size() > 0) {
            Character first = leftOverQueue.poll();
            //HACK!!!
            if(first == 0x1b)
                return new Key(Key.Kind.Escape);
            
            return new Key(first.charValue());
        }

        try {
            while(source.ready()) {
                int readChar = source.read();
                if(readChar == -1)
                    return null;

                inputBuffer.add((char)readChar);
            }
        }
        catch(IOException e) {
            throw new LanternException(e);
        }

        if(inputBuffer.size() == 0)
                return null;

        while(true) {
            //Check all patterns
            Character nextChar = inputBuffer.poll();
            boolean canMatchWithOneMoreChar = false;

            if(nextChar != null) {
                currentMatching.add(nextChar);
                for(CharacterPattern pattern: bytePatterns) {
                    if(pattern.matches(currentMatching)) {
                        if(pattern.pattern.length == currentMatching.size()) {
                            currentMatching.clear();
                            return pattern.result;
                        }
                        if(pattern.pattern.length > currentMatching.size())
                            canMatchWithOneMoreChar = true;
                    }
                }
            }
            if(!canMatchWithOneMoreChar) {
                for(Character c: currentMatching)
                    leftOverQueue.add(c);
                currentMatching.clear();
                Character first = leftOverQueue.poll();
                
                //HACK!!!
                if(first == 0x1b)
                    return new Key(Key.Kind.Escape);
                return new Key(first.charValue());
            }
        }
    }
}
