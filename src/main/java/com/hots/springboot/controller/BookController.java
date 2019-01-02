package com.hots.springboot.controller;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hots.common.util.StringUtil;
import com.hots.springboot.dao.BookDao;
import com.hots.springboot.model.Book;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @RequestMapping(value = "/list")
    public ModelAndView listAll(Book book) {
        ModelAndView mav = new ModelAndView("list");
//        if (StringUtil.isEmpty(bookName)) {
//            mav.addObject("bookList", bookDao.findAll());
//        } else {
//            mav.addObject("bookName", bookName);
//            mav.addObject("bookList", bookDao.findBookByName(bookName));
//        }

        List<Book> bookList = bookDao.findAll(new Specification<Book>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Predicate predicate = cb.conjunction();

                if (book != null) {

                    if (!StringUtil.isEmpty(book.getBookName())) {
                        predicate.getExpressions().add(cb.like(root.get("bookName"), "%" + book.getBookName() + "%"));
                    }

                }

                return predicate;
            }
        });
        mav.addObject("book", book);
        mav.addObject("bookList", bookList);
        return mav;
    }

    @RequestMapping(value = "/preAdd")
    public ModelAndView preAdd() {
        return new ModelAndView("bookAdd");
    }

    @ResponseBody
    @PostMapping(value = "/add")
    public String add(@Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldError().getDefaultMessage();
        } else {
            bookDao.save(book);
            return "redirect:/book/list";
        }
    }

    @GetMapping(value = "/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("bookUpdate");
        mav.addObject("book", bookDao.getOne(id));
        return mav;
    }

    /**
     * 修改图书
     * 
     * @param book
     * @return
     */
    @PostMapping(value = "/update")
    public String update(Book book) {
        bookDao.save(book);
        return "redirect:/book/list";
    }

    /**
     * 删除图书
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        bookDao.deleteById(id);
        return "redirect:/book/list";
    }

}
