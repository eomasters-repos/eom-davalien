/*-
 * ========================LICENSE_START=================================
 * EOMasters Validation Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 Marco Peters
 * ======================================================================
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * -> http://www.gnu.org/licenses/gpl-3.0.html
 * =========================LICENSE_END==================================
 */

package org.eomasters.gpttests.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirContentTreeVisitor extends SimpleFileVisitor<Path> {

  private final Path source;
  private final Path target;

  public CopyDirContentTreeVisitor(Path source, Path target) {
    this.source = source;
    this.target = target;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir,
      BasicFileAttributes attrs) throws IOException {
    if (dir.equals(source)) {
      return FileVisitResult.CONTINUE;
    }
    Path resolve = target.resolve(source.relativize(dir));
    if (Files.notExists(resolve)) {
      Files.createDirectories(resolve);
    }
    return FileVisitResult.CONTINUE;

  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
      throws IOException {
    Path resolve = target.resolve(source.relativize(file));
    Files.copy(file, resolve, StandardCopyOption.REPLACE_EXISTING);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) {
    System.err.format("Unable to copy: %s: %s%n", file, exc);
    return FileVisitResult.CONTINUE;
  }

}
