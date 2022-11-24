package org.javacs;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.logging.Logger;

public class CompileTask implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger("main");

    public final JavacTask task;
    public final List<CompilationUnitTree> roots;
    public final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private final Runnable close;

    public CompilationUnitTree root() {
        LOG.info("Roots size: "+roots.size());
        if (roots.size() < 1) {
            throw new RuntimeException(Integer.toString(roots.size()));
        }
        return roots.get((roots.size()-1));
    }

    public CompilationUnitTree root(Path file) {
        List<CompilationUnitTree> roots = new ArrayList<>();
        for (var root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                roots.add(root);
            }
        }
        LOG.info("Roots size for path: "+file.toString() + " : "+ +roots.size());
        if (roots.size()>0)
        return roots.get(0);
        else
        throw new RuntimeException("not found");
    }

    public CompilationUnitTree root(JavaFileObject file) {
        for (var root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                return root;
            }
        }
        throw new RuntimeException("not found");
    }

    public CompileTask(
            JavacTask task,
            List<CompilationUnitTree> roots,
            List<Diagnostic<? extends JavaFileObject>> diagnostics,
            Runnable close) {
        this.task = task;
        this.roots = roots;
        this.diagnostics = diagnostics;
        this.close = close;
    }

    @Override
    public void close() {
        close.run();
    }
}
