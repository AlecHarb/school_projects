# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake

# The command to remove a file.
RM = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/harberbarber/247repo/Lab7

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/harberbarber/247repo/Lab7/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Lab7.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Lab7.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Lab7.dir/flags.make

CMakeFiles/Lab7.dir/main.c.o: CMakeFiles/Lab7.dir/flags.make
CMakeFiles/Lab7.dir/main.c.o: ../main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/harberbarber/247repo/Lab7/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/Lab7.dir/main.c.o"
	/Library/Developer/CommandLineTools/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/Lab7.dir/main.c.o   -c /Users/harberbarber/247repo/Lab7/main.c

CMakeFiles/Lab7.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/Lab7.dir/main.c.i"
	/Library/Developer/CommandLineTools/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /Users/harberbarber/247repo/Lab7/main.c > CMakeFiles/Lab7.dir/main.c.i

CMakeFiles/Lab7.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/Lab7.dir/main.c.s"
	/Library/Developer/CommandLineTools/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /Users/harberbarber/247repo/Lab7/main.c -o CMakeFiles/Lab7.dir/main.c.s

# Object files for target Lab7
Lab7_OBJECTS = \
"CMakeFiles/Lab7.dir/main.c.o"

# External object files for target Lab7
Lab7_EXTERNAL_OBJECTS =

Lab7: CMakeFiles/Lab7.dir/main.c.o
Lab7: CMakeFiles/Lab7.dir/build.make
Lab7: CMakeFiles/Lab7.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/Users/harberbarber/247repo/Lab7/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable Lab7"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Lab7.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Lab7.dir/build: Lab7

.PHONY : CMakeFiles/Lab7.dir/build

CMakeFiles/Lab7.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Lab7.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Lab7.dir/clean

CMakeFiles/Lab7.dir/depend:
	cd /Users/harberbarber/247repo/Lab7/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/harberbarber/247repo/Lab7 /Users/harberbarber/247repo/Lab7 /Users/harberbarber/247repo/Lab7/cmake-build-debug /Users/harberbarber/247repo/Lab7/cmake-build-debug /Users/harberbarber/247repo/Lab7/cmake-build-debug/CMakeFiles/Lab7.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Lab7.dir/depend

